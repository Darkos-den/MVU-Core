package com.idexoft.mvucore.model.effect

/**
 * Only one instance of this effect can run at a time.
 * If at the moment of starting this effect the same one was running,
 * processing of the previously started effect will be stopped.
 */
interface SingleEffect