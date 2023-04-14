package com._2cha.demo.global.infra.slack;


import com._2cha.demo.global.infra.slack.config.SlackConfig;
import com._2cha.demo.global.infra.slack.payload.Attachment;
import com._2cha.demo.global.infra.slack.payload.Block;
import com._2cha.demo.global.infra.slack.payload.Divider;
import com._2cha.demo.global.infra.slack.payload.Field;
import com._2cha.demo.global.infra.slack.payload.Header;
import com._2cha.demo.global.infra.slack.payload.PlainText;
import com._2cha.demo.global.infra.slack.payload.Section;
import com._2cha.demo.global.infra.slack.payload.SlackPayload;
import com._2cha.demo.global.infra.slack.payload.Text.TextType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.MapMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class ExceptionNotificationService {

  private final SlackConfig config;
  private final ObjectMapper objectMapper;

  @Async("exNotifierTaskExecutor")
  public void send(String method, String URI, Map params, Exception ex) {
    SlackPayload payload = new SlackPayload(config.getChannel(), config.getUsername());
    String stackTrace = getStacktrace(ex);
    payload.setBlocks(makeBlocks(method, URI, params, ex));
    payload.setAttachments(makeAttachments("``` " + stackTrace + " ```"));
    payload.setIconEmoji(":robot:");

    WebClient webClient = WebClient.builder()
                                   .baseUrl(config.getEndpoint())
                                   .defaultHeader(HttpHeaders.CONTENT_TYPE,
                                                  MediaType.APPLICATION_JSON_VALUE)
                                   .build();
    webClient.post()
             .body(BodyInserters.fromValue(payload))
             .retrieve()
             .bodyToMono(String.class)
             .block(Duration.of(3, ChronoUnit.SECONDS));
  }

  private String getStacktrace(Exception ex) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    ex.printStackTrace(pw);
    return sw.toString();
  }

  private List<Block> makeBlocks(String method, String uri, Map params, Exception ex) {

    Divider divider = new Divider();
    Header title = new Header(new PlainText("Unhandled Exception Occurred"));
    Header requestInfoHeader = new Header(new PlainText("Request Info"));

    Section reqSection = new Section();
    Field methodField = new Field(TextType.MARKDOWN, "*Method*\n`" + method + "`");
    Field uriField = new Field(TextType.MARKDOWN,
                               "*URI*\n<http://localhost:8080" + uri + "|" + uri + ">");
    Field paramsField = new Field(TextType.MARKDOWN, "*Parameters*\n" + new MapMessage<>(params));
    reqSection.addFields(methodField, uriField, paramsField);

    Header exceptionInfoHeader = new Header(new PlainText("Exception Info"));
    Section exSection = new Section();
    Field exNameField = new Field(TextType.MARKDOWN,
                                  "*Exception*\n`" + ex.getClass().getSimpleName() + "`");
    Field exMessageField = new Field(TextType.MARKDOWN, "*Message*\n" + ex.getMessage());
    exSection.addFields(exNameField, exMessageField);

    return Block.build(title,
                       divider,
                       requestInfoHeader,
                       reqSection,
                       divider,
                       exceptionInfoHeader,
                       exSection,
                       divider);
  }

  private List<Attachment> makeAttachments(String text) {
    List<Attachment> attachments = new ArrayList<>();
    Attachment attachment = new Attachment();
    attachment.setColor("#e08611");
    attachment.setTitle("Stacktrace");
    attachment.setText(text);
    attachment.addMarkdownIn("text");

    attachments.add(attachment);

    return attachments;
  }
}


