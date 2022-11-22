package com.noti.noti.error.exception;

import com.noti.noti.error.ErrorCode;

public class EntityNotFoundException extends BusinessException{

  public EntityNotFoundException(String message) {
    super(message, ErrorCode.ENTITY_NOT_FOUND);
  }

  public EntityNotFoundException(String message, ErrorCode errorCode) {
    super(message, errorCode);
  }
}
