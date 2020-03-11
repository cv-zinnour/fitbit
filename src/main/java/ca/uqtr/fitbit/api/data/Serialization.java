package ca.uqtr.fitbit.api.data;

import lombok.Data;

@Data
class Serialization{
    private String dateTime;
    private int value;
    private String dataset;
    private int datasetInterval;

    public Serialization() {
    }

    public Serialization(String dateTime, int value, String dataset, int datasetInterval) {
        this.dateTime = dateTime;
        this.value = value;
        this.dataset = dataset;
        this.datasetInterval = datasetInterval;
    }
}
