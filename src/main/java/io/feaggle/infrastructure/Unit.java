/*
 * Copyright (c) 2019-present, Kevin Mas Ruiz
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package io.feaggle.infrastructure;

public enum Unit {
    B(1),
    KB(1000),
    MB(1000000),
    GB(1000000000),
    TB(1000000000000L);

    public final long conversion;

    Unit(long conversion) {
        this.conversion = conversion;
    }
}