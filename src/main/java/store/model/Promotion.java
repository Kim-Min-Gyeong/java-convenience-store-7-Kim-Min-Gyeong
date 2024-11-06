package store.model;

import camp.nextstep.edu.missionutils.DateTimes;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Promotion {
    private final String name;

    private final Integer buyQuantity;

    private final Integer getQuantity;

    private final LocalDate startDate;

    private final LocalDate endDate;


    public Promotion(String name, Integer buyQuantity, Integer getQuantity, LocalDate startDate, LocalDate endDate){
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.getQuantity = getQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public Integer getBuyQuantity() {
        return buyQuantity;
    }

    public Integer getGetQuantity() {
        return getQuantity;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
