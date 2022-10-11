package com.axreng.backend;

import com.axreng.backend.application.match.HrefHtmlPatternMatch;
import com.axreng.backend.application.match.KeywordPatternMatch;
import com.axreng.backend.application.match.MatchHandler;
import com.axreng.backend.application.pattern.HrefHtmlPattern;
import com.axreng.backend.application.pattern.KeywordPattern;
import com.axreng.backend.application.search.SearchService;
import com.axreng.backend.domain.commons.events.EventDispatcher;
import com.axreng.backend.domain.commons.events.IEventDispatcher;
import com.axreng.backend.domain.commons.events.IEventHandler;
import com.axreng.backend.domain.search.entity.Search;
import com.axreng.backend.domain.search.events.KeywordFound;
import com.axreng.backend.domain.search.events.KeywordFoundHandler;
import com.axreng.backend.domain.search.events.UrlFound;
import com.axreng.backend.domain.search.events.UrlFoundHandler;


public class Main {
    public static void main(String[] args) {
        /*get("/crawl/:id", (req, res) ->
                "GET /crawl/" + req.params("id"));
        post("/crawl", (req, res) ->
                "POST /crawl" + System.lineSeparator() + req.body());*/

        try{
            //#region Create a search 
            final String url = System.getenv("BASE_URL");
            final String keyword = System.getenv("KEYWORD");
            final String limitOfResults = System.getenv("MAX_RESULTS");
            //Search search = new Search(url, keyword, limitOfResults);
            Search search = new Search("http://hiring.axreng.com/", "four", "8");
            //#endregion

            //#region Dispatcher
            IEventDispatcher dispatcher = new EventDispatcher();
            IEventHandler<KeywordFound> keywordFoundHandler = new KeywordFoundHandler();
            dispatcher.subscribe(KeywordFound.class, keywordFoundHandler);
            IEventHandler<UrlFound> urlFoundHandler = new UrlFoundHandler();
            dispatcher.subscribe(UrlFound.class, urlFoundHandler);
            //#endregion

            //#region MatchHandler
            KeywordPatternMatch keywordPatternMatch = new KeywordPatternMatch(dispatcher, new KeywordPattern(search.keyword()));
            HrefHtmlPatternMatch hrefHtmlPatternMatch = new HrefHtmlPatternMatch(dispatcher, new HrefHtmlPattern());
            MatchHandler matchHandler = new MatchHandler(search, keywordPatternMatch, hrefHtmlPatternMatch);
            //#endregion

            SearchService service = new SearchService(matchHandler);
            service.execute(search);
        }catch(Exception e){
            System.exit(0);
        }  
    }
}