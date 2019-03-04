/*
 * Copyright (c) 2019-present, Kevin Mas Ruiz
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package io.feaggle.driver;

public interface DrivenBy<Context> {
    String identifier();
    void drive(Context context);
}
