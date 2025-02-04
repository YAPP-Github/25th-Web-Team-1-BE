package org.openaiclient.client;

import java.util.concurrent.CompletableFuture;

import org.openaiclient.client.dto.request.ChatCompletionRequest;
import org.openaiclient.client.dto.response.ChatCompletionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
public class OpenAiClient {

	private final RestClient openAiClient;

	public OpenAiClient(
		@Value("${client.openai.url}") String url,
		@Value("${client.openai.key}") String key
	) {
		this.openAiClient = RestClient.builder()
			.baseUrl(url)
			.defaultHeader("Authorization", "Bearer " + key)
			.defaultHeader("Content-Type", "application/json")
			.build();
	}

	public ChatCompletionResponse getChatCompletion(ChatCompletionRequest chatCompletionRequest) {
		try {
			return openAiClient.post()
				.body(chatCompletionRequest)
				.retrieve()
				.body(ChatCompletionResponse.class);
		} catch (RestClientResponseException e) {
			String errorBody = e.getResponseBodyAsString();
			throw new RuntimeException("Client error: " + e.getStatusCode() + " " + errorBody);
		}
	}

	@Async
	public CompletableFuture<ChatCompletionResponse> getChatCompletionAsync(
		ChatCompletionRequest chatCompletionRequest
	) {
		return CompletableFuture.completedFuture(getChatCompletion(chatCompletionRequest));
	}
}
