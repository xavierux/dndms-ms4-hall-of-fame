package com.xvclemente.dnd.ms4.config;

import com.xvclemente.dnd.ms4.model.RankingEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ListTablesResponse;

@Configuration
@Slf4j
@Profile("!test")
public class DynamoDbInitializer {

    @Bean
    ApplicationRunner applicationRunner(DynamoDbClient dynamoDbClient,
                                        DynamoDbEnhancedClient enhancedClient,
                                        @Value("${app.dynamodb.table-name.rankings}") String rankingsTableName) {
        return args -> {
            ListTablesResponse response = dynamoDbClient.listTables();
            if (!response.tableNames().contains(rankingsTableName)) {
                log.info("Tabla '{}' no encontrada. Creándola...", rankingsTableName);
                enhancedClient.table(rankingsTableName, TableSchema.fromBean(RankingEntry.class)).createTable();
                log.info("Tabla '{}' creada.", rankingsTableName);
            } else {
                log.info("Tabla '{}' ya existe.", rankingsTableName);
            }
        };
    }
}