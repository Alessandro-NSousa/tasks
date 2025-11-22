package com.tarefas.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Service
public class IAService {
    @Value("${groq.api.key}")
    private String groqApiKey;

    @Value("${groq.api.model}")
    private  String model;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public String gerarResposta(String prompt) {
        try {
            String jsonBody = """
                    { "model": "%s",
                    "messages": [
                        { "role": "user", "content": "%s" }
                    ]
                    }
                    """.formatted(model,prompt.replace("\"", "\\\""));

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + groqApiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                "https://api.groq.com/openai/v1/chat/completions",
                HttpMethod.POST,
                entity,
                String.class
            );

            JsonNode json = mapper.readTree(response.getBody());

            return json.get("choices").get(0).get("message").get("content").asText();

        } catch (Exception e) {
            return "Erro ao chamar IA: " + e.getMessage();
        }

    }

    public Map<String, String> gerarTarefa(String textoDoUsuario) {
        try {
            String prompt = """
                    Voce é uma IA criadora de tarefas.
                    O usuário enviou: "%s".
                    
                    Gere:
                    - um título curto e objetivo
                    - uma descriçao clara e útil
                    
                    Responda exatamente neste formato JSON:
                    
                    {
                        "titulo": "texto aqui",
                        "descriçao": "texto aqui
                    }
                    """.formatted(textoDoUsuario.replace("\"", "\\\""));

            String resposta = gerarResposta(prompt);

            JsonNode json = mapper.readTree(resposta);

            Map<String, String> resultado = new HashMap<>();
            resultado.put("titulo", json.get("titulo").asText());
            resultado.put("descriçao", json.get("titulo").asText());

            return resultado;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar tarefa com IA: " + e.getMessage());
        }
    }
}
