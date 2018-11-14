package com.wuhao.email.excptionHandler;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@ResponseBody
public class MyExceptionHandler {

    @ExceptionHandler(MyException.class)
    public ModelAndView exceptionHandler(MyException e, Model model){
        model.addAttribute("code",e.getErrorCode());
        model.addAttribute("message",e.getErrorMessage());
        return new ModelAndView("common/error");
    }
}
