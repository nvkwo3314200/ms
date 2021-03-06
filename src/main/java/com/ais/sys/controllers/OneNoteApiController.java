package com.ais.sys.controllers;

import com.ais.sys.models.Quote;
import com.ais.sys.services.QuoteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/onenote")
public class OneNoteApiController {

    @Resource
    QuoteService quoteService;

    @GetMapping
    public Quote getOneNote() {
        Quote randomOne = quoteService.findRandomOne();
        Quote quote = new Quote();
        quote.setAuthor(randomOne.getAuthor());
        quote.setQuote(randomOne.getQuote());
        return quote;
    }
}
