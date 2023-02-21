package org.code.lld.model;

import java.math.BigDecimal;

public record Inventory(
        Integer id,
        String desc,
        BigDecimal cost
) {
}
