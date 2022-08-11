package vn.nextpay.nextshop.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import vn.nextpay.nextshop.enums.EResponseStatus;
import vn.nextpay.nextshop.exception.NextshopException;
import vn.nextpay.nextshop.security.SecurityUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Slf4j
public class RestFactory {
    private static String NP_USER_ID = SecurityUtils.getCurrentActor().get().getUserId();
    private static  String NP_LOCATION_ID = System.getenv("NP_LOCATION_ID");

    public static <T> T invokePostRequest(String url,
                                          Object body,
                                          ParameterizedTypeReference<T> responseType,
                                          String token, HttpServletRequest servletRequest) throws NextshopException {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            httpHeaders.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
//            httpHeaders.setBearerAuth(token.replace("Bearer ", ""));
            log.info("====== NP_LOCATION_ID: "+ NP_LOCATION_ID);
            log.info("====== NP_USER_ID: "+ NP_USER_ID);
            httpHeaders.set("np-merchant-id", token);
            httpHeaders.set("np-location-id", NP_LOCATION_ID);
            httpHeaders.set("np-user-id", NP_USER_ID);
            httpHeaders.set("np-app-id", servletRequest.getHeader("np-app-id"));
            httpHeaders.set("np-anonymous", "true");
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<Object> request = new HttpEntity<>(body, httpHeaders);
            ResponseEntity<T> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    responseType
            );
            if (HttpStatus.OK.equals(response.getStatusCode()) && response.getBody() != null) {
                return response.getBody();
            }
            throw new NextshopException(EResponseStatus.INTERNAL_SERVER_ERROR);
        } catch (RestClientException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static <T> T invokePatchRequest(String url,
                                           Object body,
                                           ParameterizedTypeReference<T> responseType,
                                           String token, HttpServletRequest servletRequest) throws NextshopException {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            httpHeaders.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
//            httpHeaders.setBearerAuth(token.replace("Bearer ", ""));
            httpHeaders.set("np-merchant-id", token);
            log.info("====== NP_LOCATION_ID: "+ NP_LOCATION_ID);
            log.info("====== NP_USER_ID: "+ NP_USER_ID);
            httpHeaders.set("np-location-id", NP_LOCATION_ID);
            httpHeaders.set("np-user-id", NP_USER_ID);
            httpHeaders.set("np-app-id", servletRequest.getHeader("np-app-id"));
            httpHeaders.set("np-anonymous", "true");
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(requestFactory);
            HttpEntity<Object> request = new HttpEntity<>(body, httpHeaders);
            ResponseEntity<T> response = restTemplate.exchange(
                    url,
                    HttpMethod.PATCH,
                    request,
                    responseType
            );
            if (HttpStatus.OK.equals(response.getStatusCode()) && response.getBody() != null) {
                return response.getBody();
            }
            throw new NextshopException(EResponseStatus.INTERNAL_SERVER_ERROR);
        } catch (RestClientException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static <T> T invokePostRequestExport(String url,
                                          Object body,
                                          Class<T> clazz,
                                          String token, HttpServletRequest servletRequest) throws NextshopException {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            httpHeaders.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
            //httpHeaders.setBearerAuth(token);
            httpHeaders.setBearerAuth(servletRequest.getHeader(HttpHeaders.AUTHORIZATION).replace("Bearer ", ""));
            httpHeaders.set("np-location-id", servletRequest.getHeader("np-location-id"));
            httpHeaders.set("np-user-id", servletRequest.getHeader("np-user-id"));
            httpHeaders.set("np-app-id", servletRequest.getHeader("np-app-id"));
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<Object> request = new HttpEntity<>(body, httpHeaders);
            ResponseEntity<T> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    clazz
            );
            if (HttpStatus.OK.equals(response.getStatusCode()) && response.getBody() != null) {
                return response.getBody();
            }
            throw new NextshopException(EResponseStatus.INTERNAL_SERVER_ERROR);
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new NextshopException(EResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static <T> T exchangeGetRequest(String url,
                                           Object body,
                                           Class<T> clazz,
                                           String token) throws NextshopException {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            httpHeaders.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
            httpHeaders.setBearerAuth(token);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<Object> request = new HttpEntity<>(httpHeaders);
            ResponseEntity<T> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    clazz
            );
            if (HttpStatus.OK.equals(response.getStatusCode()) && response.getBody() != null) {
                return response.getBody();
            }
            throw new NextshopException(EResponseStatus.INTERNAL_SERVER_ERROR);
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new NextshopException(EResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static <T> HttpResponseObject<T> invokeDeleteRequest(String url,
                                                                ParameterizedTypeReference<HttpResponseObject<T>> responseType,
                                                                String token) throws NextshopException {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            httpHeaders.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
            httpHeaders.setBearerAuth(token);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<Object> request = new HttpEntity<>(httpHeaders);
            ResponseEntity<HttpResponseObject<T>> response = restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    request,
                    responseType
            );
            if (HttpStatus.OK.equals(response.getStatusCode()) && response.getBody() != null) {
                return response.getBody();
            }
            throw new NextshopException(EResponseStatus.INTERNAL_SERVER_ERROR);
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new NextshopException(EResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static <T> T invokeGetRequest(String url, String token,
                                         HttpServletRequest servletRequest,
                                         ParameterizedTypeReference<T> responseType) throws NextshopException {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            httpHeaders.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
//            httpHeaders.setBearerAuth(token.replace("Bearer ", ""));
            httpHeaders.set("np-merchant-id", token);
            httpHeaders.set("np-location-id", NP_LOCATION_ID);
            httpHeaders.set("np-user-id", NP_USER_ID);
            httpHeaders.set("np-app-id", servletRequest.getHeader("np-app-id"));
            httpHeaders.set("np-anonymous", "true");
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<Object> request = new HttpEntity<>(httpHeaders);
            ResponseEntity<T> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    responseType
            );
            if (HttpStatus.OK.equals(response.getStatusCode()) && response.getBody() != null) {
                return response.getBody();
            }
            throw new NextshopException(EResponseStatus.INTERNAL_SERVER_ERROR);
        } catch (RestClientException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
