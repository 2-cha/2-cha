package com._2cha.demo.global.config;

import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class UppercaseNamingStrategy extends CamelCaseToUnderscoresNamingStrategy {


  @Override
  public Identifier toPhysicalTableName(Identifier logicalName, JdbcEnvironment jdbcEnvironment) {
    return toUpper(super.toPhysicalTableName(logicalName, jdbcEnvironment));
  }

  @Override
  public Identifier toPhysicalColumnName(Identifier logicalName, JdbcEnvironment jdbcEnvironment) {
    return toUpper(super.toPhysicalTableName(logicalName, jdbcEnvironment));
  }

  private Identifier toUpper(Identifier name) {
    if (name == null) {
      return null;
    }
    if (name.isQuoted()) {
      return name;
    }
    String text = name.getText().toUpperCase();
    return Identifier.toIdentifier(text);
  }
}
