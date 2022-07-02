package co.com.bancolombia.model.esubscription;
import co.com.bancolombia.model.enterprise.Enterprise;
import co.com.bancolombia.model.enterprisevalidation.EnterpriseValidation;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ESubscription {
    private EnterpriseValidation validation;
    private Enterprise enterprise;
    private Agreement agreement;

    public static ESubscription createSubscription(EnterpriseValidation enterpriseValidation) {
        Agreement agreement = Agreement.builder().build();
        return ESubscription.builder()
                .validation(enterpriseValidation)
                .enterprise(enterpriseValidation.getEnterprise())
                .agreement(agreement)
                .build();
    }
}
