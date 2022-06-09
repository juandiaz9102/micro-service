package com.nttdata.som.serviceorder.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.exceptions.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
/**
 *
 * @author fhernanq
 * Clase que crea un cliente OkHttp para consumir servicios GraphQl
 */
public class ClientUpdate {

	public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

	/**
	 *
	 * @param graphQLRequest peqticion request formato graphQL
	 * @param url endPoint de la peticion
	 * @return
	 * @throws ApplicationException
	 * @throws Exception
	 */
	public JsonNode ejecutar(String graphQLRequest, String url) throws IOException, ApplicationException {
		OkHttpClient client = null;
		RequestBody body = null;
		Request request = null;
		Response response = null;

//		try {
		client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS)
				.writeTimeout(10, TimeUnit.SECONDS).build();
		log.info("Request "+graphQLRequest);
		body = RequestBody.create(JSON, graphQLRequest);
		request = new Request.Builder().url(url).post(body).build();
		response = client.newCall(request).execute();

		/*
		 * Se recupera el body de la peticion antes de validar debido a que al realizar el llamado al response.body().string() el response se cierra
		 * Se valida si la respuesta fue de error para lanzar la excepcion
		 */
		ObjectMapper objMaper = new ObjectMapper();
		JsonNode jsonNode = objMaper.readTree(response.body().string());
		JsonNode error =jsonNode.get("errors");

		if(error != null) {
			throw new ApplicationException(objMaper.treeToValue(error.get(0).get("message"),String.class));
		}
		log.info("Response Ok "+jsonNode);
		return jsonNode;
//		} catch (Exception e) {			
//			throw e;
//		} finally {			
//			if(response != null) {
//				response.close();
//			}
//		}
	}
}
