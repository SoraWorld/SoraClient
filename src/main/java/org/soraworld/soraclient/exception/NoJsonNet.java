/*******************************************************************************
 * Created by Himmelt on 2016/9/24.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.exception;

import org.soraworld.soraclient.ui.DialogStage;

public class NoJsonNet extends Throwable {

    public void dispose() {
        DialogStage stage = new DialogStage();
        stage.show();
    }
}
