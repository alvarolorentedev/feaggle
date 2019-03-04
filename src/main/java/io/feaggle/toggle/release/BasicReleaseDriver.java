/*
 * Copyright (c) 2019-present, Kevin Mas Ruiz
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package io.feaggle.toggle.release;

import lombok.Builder;
import lombok.Singular;

import java.util.Map;

@Builder
public class BasicReleaseDriver implements ReleaseDriver {
    @Singular
    private final Map<String, Boolean> releases;

    public boolean isFlaggedForRelease(String feature) {
        return releases.getOrDefault(feature, false);
    }
}