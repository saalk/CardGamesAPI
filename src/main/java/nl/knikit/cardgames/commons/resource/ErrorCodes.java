package nl.knikit.cardgames.commons.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ErrorCodes {
    /**
     * Business Rules
     */
    CARD_STATUS_IS_NOT_ACTIVATED_RULE                   (501),
    CARD_STATUS_IS_NOT_TEMP_BLOCKED_RULE                (502),
    CARD_STATUS_IS_NOT_INVALID_PIN_RULE                 (503),
    ACCOUNT_STATUS_IS_NOT_BLOCKED_RULE                  (504),
    REISSUE_PERIOD_RULE                                 (505),
    MAXIMUM_REQUESTS_PER_YEAR_RULE                      (506),
    ACCOUNT_STATUS_IS_NOT_CLOSED_RULE                   (507),
    ACCOUNT_STATUS_IS_NOT_CANCELLED_RULE                (508),
    ACCOUNT_IS_NOT_STUDENT_RULE                         (509),
    REQUESTOR_HAS_ROLE_RULE                             (510),
    CARD_PORTFOLIO_CODE_EQUALS_RULE                     (511),
    ACCOUNT_IS_NOT_AFBETALING_CONTINU_LIMIET_RULE       (512),
    ACCOUNT_IS_NOT_OVERSTAPSERVICE_GAANDE_REKENING_RULE (513),
    ACCOUNT_IS_NOT_STUDENT_RULE_SIA                     (514),
    APPLY_REPRICING_DAYS_SINCE_RULE                     (515),
    CARD_STATUS_IS_NOT_BLOCKED_RULE                     (516),
    DAYS_SINCE_RULE                                     (517),
    PENDING_REQUESTS_RULE                               (518),
    CARD_VALIDITY_REMAINING_RULE                        (519),
    ACTIVATION_TOO_CLOSE_TO_EXPIRATION_RULE             (520),
    CARD_IS_NOT_ACTIVATED_RULE                          (521),
    CARD_STATUS_IS_NOT_CLOSED_RULE                      (522),

    /**
     * Remaining error codes
     */
    CONCURRENT_CARDGAME          (601),
    INVALID_CARDGAME             (602),
    VALIDATION_FAILED            (603),
    RUNTIME_EXCEPTION            (701),
    WEB_APPLICATION_EXCEPTION    (900);

    @Getter
    private int status;

    @Override
    public String toString() {
        return String.valueOf(status);
    }
}
