/*
 * Author : Anisha Palei
 * Date : 10-03-2023
 * Created with : IntelliJ IDEA Community Edition
 */

package com.niit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND , reason = "Task Not Found")
public class TaskNotFoundException extends  Exception{
}
