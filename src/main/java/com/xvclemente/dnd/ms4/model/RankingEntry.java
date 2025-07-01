package com.xvclemente.dnd.ms4.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@NoArgsConstructor
@DynamoDbBean
public class RankingEntry {

    private String entityId;
    private int victories = 0;
    private int gold = 0;

    public RankingEntry(String entityId) {
        this.entityId = entityId;
    }

    @DynamoDbPartitionKey
    public String getEntityId() {
        return entityId;
    }
}
