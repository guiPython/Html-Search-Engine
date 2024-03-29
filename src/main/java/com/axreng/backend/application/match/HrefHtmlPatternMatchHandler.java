package com.axreng.backend.application.match;

import java.net.MalformedURLException;

import com.axreng.backend.application.pattern.HrefHtmlPattern;
import com.axreng.backend.domain.commons.events.IEventDispatcher;
import com.axreng.backend.domain.query.IQuery;
import com.axreng.backend.domain.search.entity.ISearch;
import com.axreng.backend.domain.search.events.UrlFound;

public class HrefHtmlPatternMatchHandler implements IPatternMatchHandler{
    private final  IEventDispatcher dispatcher;
    private final HrefHtmlPattern regex;

    public HrefHtmlPatternMatchHandler(IEventDispatcher dispatcher, HrefHtmlPattern pattern){
        this.dispatcher = dispatcher;
        this.regex = pattern;
    }

    @Override
    public synchronized void process(String document, ISearch search, IQuery query) {
        for(var match : this.regex.matches(document)){
            try{
                UrlFound event = new UrlFound(search, search.url().concat(match).toString());
                dispatcher.notify(event);
            }catch(MalformedURLException e){
                System.out.println(e.getMessage());
            }
        }
    }
    
}
