package nexign_project_maven.brt.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name = "tariffs")
@JsonIgnoreProperties({"name"})
public class Tariff {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "call_rate_same_operator")
    @JsonProperty
    private BigDecimal callRateSameOperator;

    @Column(name = "call_rate_other_operator")
    @JsonProperty
    private BigDecimal callRateOtherOperator;

    @Column(name = "free_incoming_minutes")
    @JsonProperty
    private Integer freeIncomingMinutes;

    @Column(name = "monthly_fee")
    @JsonProperty
    private BigDecimal monthlyFee;

    public Long getId() {
        return id;
    }
}
