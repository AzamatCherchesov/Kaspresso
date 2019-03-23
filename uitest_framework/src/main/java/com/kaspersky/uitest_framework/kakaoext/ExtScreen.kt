package com.kaspersky.uitest_framework.kakaoext

import android.view.View
import android.support.test.espresso.Espresso
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers
import com.agoda.kakao.common.KakaoDslMarker
import com.agoda.kakao.configurator.KakaoConfigurator
import com.agoda.kakao.screen.ScreenActions

/**
 * An extension of [com.agoda.kakao.screen.Screen].
 */
@Suppress("UNCHECKED_CAST")
@KakaoDslMarker
abstract class ExtScreen<out T : ExtScreen<T>> : ScreenActions {

    /**
     * The layout id with which the screen is associated.
     */
    abstract val layoutId: Int?

    /**
     * The view class with which the screen is associated.
     */
    abstract val viewClass: Class<out Any>?

    override val view = KakaoConfigurator.createViewInteractionDelegate(
        Espresso.onView(ViewMatchers.isRoot())
    )

    operator fun invoke(function: T.() -> Unit) = function.invoke(this as T)

    companion object {
        /**
         * Idles for given amount of time
         *
         * @param duration Time to idle in milliseconds (1 second by default)
         */
        fun idle(duration: Long = 1000L) {
            Espresso.onView(ViewMatchers.isRoot()).perform(object : ViewAction {
                override fun getDescription() = "Idle for $duration milliseconds"

                override fun getConstraints() = ViewMatchers.isAssignableFrom(View::class.java)

                override fun perform(uiController: UiController?, view: View?) {
                    uiController?.loopMainThreadForAtLeast(duration)
                }
            })
        }

        inline fun <reified T : ExtScreen<T>> onScreen(function: T.() -> Unit): T =
            T::class.java.newInstance().apply { function.invoke(this) }
    }
}