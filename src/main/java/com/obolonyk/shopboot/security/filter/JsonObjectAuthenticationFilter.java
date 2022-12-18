package com.obolonyk.shopboot.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obolonyk.shopboot.security.model.LoginCredentials;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringJoiner;

public class JsonObjectAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try{
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine())!=null){
                sb.append(line);
            }
            //sb should be in json format
            String content = sb.toString();

            //if the content came from html form
            if (!content.startsWith("{")){
                String stringInJsonFormat = getStringInJsonFormat(content);
                content = stringInJsonFormat;
            }

            LoginCredentials authRequest = objectMapper.readValue(content, LoginCredentials.class);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    authRequest.getLogin(),
                    authRequest.getPassword());
            setDetails(request, token);
            return this.getAuthenticationManager().authenticate(token);
        } catch (IOException e){
            throw new RuntimeException();
        }
    }

    private static String getStringInJsonFormat(String s) {
        String replace = s.replace("=", ":");
        String[] split = replace.split("&");
        StringJoiner sj = new StringJoiner(",", "{", "}");
        for (String str : split) {
            String conv = convertToJsonPattern(str);
            sj.add(conv);
        }
        String x = sj.toString();
        return x;
    }

    private static String convertToJsonPattern (String s){
        String[] split = s.split(":");
        StringJoiner sj = new StringJoiner(":");
        for (String str : split) {
            sj.add("\"" + str + "\"");
        }
        return sj.toString();
    }

}
