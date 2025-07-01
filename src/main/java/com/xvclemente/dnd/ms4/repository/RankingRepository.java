package com.xvclemente.dnd.ms4.repository;

import com.xvclemente.dnd.ms4.model.RankingEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RankingRepository {

    private final DynamoDbTable<RankingEntry> rankingTable;

    @Autowired
    public RankingRepository(DynamoDbEnhancedClient enhancedClient,
                             @Value("${app.dynamodb.table-name.rankings}") String tableName) {
        this.rankingTable = enhancedClient.table(tableName, TableSchema.fromBean(RankingEntry.class));
    }

    public void save(RankingEntry rankingEntry) {
        rankingTable.putItem(rankingEntry);
    }

    public Optional<RankingEntry> findById(String entityId) {
        return Optional.ofNullable(rankingTable.getItem(Key.builder().partitionValue(entityId).build()));
    }

    public List<RankingEntry> findAll() {
        // ¡Cuidado! Un 'scan' en DynamoDB puede ser lento y costoso con muchos datos.
        // Para nuestro caso de uso con un ranking limitado, es aceptable.
        return rankingTable.scan().items().stream().collect(Collectors.toList());
    }
}