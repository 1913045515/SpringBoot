package com.example.demo.controller;
/**
 *
 * @author linzhiqiang
 * @date 2018/8/7
 */


@RestController
@RequestMapping("log4j")
public class Log4j2Controller {
    private static final Logger logger = LoggerFactory.getLogger(Log4j2Controller.class);
    @RequestMapping("test")
    public boolean test(){
        logger.trace("trace");
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
        return true;
    }
}
