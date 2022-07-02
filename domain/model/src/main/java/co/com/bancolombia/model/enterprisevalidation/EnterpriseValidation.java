package co.com.bancolombia.model.enterprisevalidation;
import co.com.bancolombia.model.enterprise.CreditState;
import co.com.bancolombia.model.enterprise.Enterprise;
import co.com.bancolombia.model.enterprise.SuperIntReport;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class EnterpriseValidation {
    private Enterprise enterprise;
    private boolean nitIsValid;
    private boolean hasRestrictions;
    private CreditState creditState;
    private SuperIntReport superIntReport;

}
