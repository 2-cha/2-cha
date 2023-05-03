package com._2cha.demo.collection.exception;

import com._2cha.demo.global.exception.NotFoundException;

public class NoSuchCollectionException extends NotFoundException {

  public NoSuchCollectionException() {super("No collection with such id.", "noSuchCollection");}
}
