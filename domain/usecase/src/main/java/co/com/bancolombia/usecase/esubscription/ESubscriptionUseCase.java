package co.com.bancolombia.usecase.esubscription;

import co.com.bancolombia.model.enterprise.*;
import co.com.bancolombia.model.enterprise.gateways.EnterpriseService;
import co.com.bancolombia.model.enterprisevalidation.EnterpriseValidation;
import co.com.bancolombia.model.esubscription.ESubscription;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

@RequiredArgsConstructor
public class ESubscriptionUseCase {

    private final EnterpriseService enterpriseService;

    public Mono<ESubscription> suscribeEnterprise(Enterprise enterprise){
        return enterpriseService.validateEnterprise(enterprise)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new EnterpriseNotFoundException())))
                .flatMap(this::searchRestrictions)
                .flatMap(this::searchCreditStateAndReports)
                .map(this::addInfoToValidation)
                .map(ESubscription::createSubscription)
                .onErrorMap((e) -> !(e instanceof EnterpriseNotFoundException),
                        (e2) -> new EnterpriseNotValidException(e2.getMessage()));
    }

    private EnterpriseValidation addInfoToValidation(Tuple3<CreditState, SuperIntReport, EnterpriseValidation> tuple) {
        return tuple.getT3().toBuilder().enterprise(tuple.getT3().getEnterprise())
                .creditState(tuple.getT1())
                .superIntReport(tuple.getT2())
                .build();
    }

    private Mono<Tuple3<CreditState, SuperIntReport, EnterpriseValidation>> searchCreditStateAndReports(EnterpriseValidation enterpriseValidation) {
        return Mono.zip(enterpriseService.searchCreditState(enterpriseValidation.getEnterprise()),
                enterpriseService.searchSuperIntReports(enterpriseValidation.getEnterprise()),
                Mono.just(enterpriseValidation));
    }

    private Mono<EnterpriseValidation> searchRestrictions(EnterpriseValidation ev) {
        return enterpriseService.searchRestrictions(ev.getEnterprise())
                .map((e) -> {
                    boolean hasRestrictions = e.getRestrictions().size() != 0;
                    return ev.toBuilder()
                            .enterprise(ev.getEnterprise())
                            .hasRestrictions(hasRestrictions)
                            .build();
                });
    }
}
