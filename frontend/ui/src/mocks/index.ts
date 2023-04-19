import {
  setupWorker,
  type MockedRequest,
  type RestHandler,
  type DefaultBodyType,
} from 'msw';
import { setupServer } from 'msw/node';
import { serverHandlers } from './handlers/server';
import { kakaoHandlers } from './handlers/kakao';

const handlers: RestHandler<MockedRequest<DefaultBodyType>>[] = [
  ...serverHandlers,
  ...kakaoHandlers,
];

function initMocks() {
  if (typeof window === 'undefined') {
    const server = setupServer(...handlers);
    server.listen();
  } else {
    const worker = setupWorker(...handlers);
    worker.start();
  }
}

initMocks();

export {};
