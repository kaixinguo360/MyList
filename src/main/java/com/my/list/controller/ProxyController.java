package com.my.list.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.Enumeration;

@Controller
@RequestMapping("/p")
public class ProxyController {

    private final RestTemplate rest;
    private Base64.Encoder encoder;
    private Base64.Decoder decoder;

    public ProxyController() {
        this.rest = new RestTemplate();
        this.encoder = Base64.getUrlEncoder();
        this.decoder = Base64.getUrlDecoder();
    }

    @ResponseBody
    @RequestMapping(value = "/static/{url64}")
    protected ResponseEntity<byte[]> proxyStaticAssets(HttpServletRequest request,
                                                @PathVariable String url64) throws RequestException {

        String url = decodeBase64Url(url64);

        HttpHeaders headers = getHeaders(request);
        HttpEntity<String> requestEntity = new HttpEntity<>("", headers);

        try {
            return rest.exchange(url, HttpMethod.GET, requestEntity, byte[].class);
        } catch (RestClientResponseException e) {
            HttpStatus status = HttpStatus.resolve(e.getRawStatusCode());
            return new ResponseEntity<>(e.getResponseBodyAsByteArray(), status == null ? HttpStatus.INTERNAL_SERVER_ERROR : status);
        } catch (ResourceAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/page/{url64}")
    protected ResponseEntity<byte[]> proxyPage(HttpServletRequest request,
                                               @PathVariable String url64) throws RequestException {

        String urlStr = decodeBase64Url(url64);
        URL url;
        Document doc;

        try {
            url = new URL(urlStr);
            doc = Jsoup.connect(urlStr).get();
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Elements scripts = doc.select("script[src]");
        for (Element script : scripts) {
            String src = script.absUrl("src");
            if (!StringUtils.isEmpty(src)) {
                script.attr("src", src);
            }
        }

        Elements links = doc.select("link[href]");
        for (Element link : links) {
            String href = link.absUrl("href");
            link.attr("href", href);
        }

        Elements as = doc.select("a[href]");
        for (Element s : as) {
            String href = s.absUrl("href");
            s.attr("href", "/p/page/" + encodeBase64Url(href));
        }

        addHook(doc, url);

        return new ResponseEntity<>(doc.toString().getBytes(), HttpStatus.OK);
    }

    private static final String header = "<script>BASE='%s'</script>\n" +
        "<script>PATH='%s'</script>\n" +
        "<script src='//code.jquery.com/jquery-3.3.1.min.js'></script>\n" +
        "<script src='//unpkg.com/xhook@latest/dist/xhook.min.js'></script>\n" +
        "<link rel='stylesheet' href='/p/hook/hook.css'></link>\n" +
        "<script src='/p/hook/hook.js'></script>\n";
    private void addHook(Document doc, URL url) {
        String protocol = url.getProtocol() + "://";
        String host = url.getHost();
        String port = url.getPort() == -1 ? "" : ":" + url.getPort();
        String base = protocol + host + port;
        doc.head().prepend(String.format(header, base, url.getFile()));
    }

    private String encodeBase64Url(String url) throws RequestException {
        try {
            if (url != null) {
                return new String(encoder.encode(url.getBytes()));
            } else {
                return "";
            }
        } catch (IllegalArgumentException e) {
            throw new RequestException("Bad Request", HttpStatus.BAD_REQUEST);
        }
    }

    private String decodeBase64Url(String url64) throws RequestException {
        try {
            return new String(decoder.decode(url64));
        } catch (IllegalArgumentException e) {
            throw new RequestException("Bad Request", HttpStatus.BAD_REQUEST);
        }
    }

    private HttpHeaders getHeaders(HttpServletRequest request) {

        HttpHeaders headers = new HttpHeaders();
        Enumeration headerNames = request.getHeaderNames();

        while(headerNames!=null && headerNames.hasMoreElements()) {
            String headerName = (String) headerNames.nextElement();
            String headerValue = request.getHeader(headerName);

            if(headerValue != null) {
                headers.add(headerName, headerValue);
            }
        }

        return headers;
    }
}
