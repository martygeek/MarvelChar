package us.martypants.marvel

import android.app.Application
import android.content.Context
import us.martypants.marvel.modules.DaggerUserComponent
import us.martypants.marvel.modules.NetModule
import us.martypants.marvel.modules.UserComponent
import us.martypants.marvel.modules.UserModule

class App : Application() {
    private var mUserComponent: UserComponent? = null
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        if (mUserComponent == null) {
            mUserComponent = DaggerUserComponent.builder()
                .userModule(UserModule())
                .netModule(NetModule(this, networkEndpoint))
                .build()
        }
        app = this
        mUserComponent?.inject(this)
    }

    private val networkEndpoint: String
        get() = "https://gateway.marvel.com:443/"
    /**
     * Get the UserComponent to inject a class with
     *
     * @return a UserComponent
     */
    /**
     * This is used to inject a component for testing if
     * required.
     *
     * @param userComponent a usercomponent for testing with
     */
    var userComponent: UserComponent?
        get() = mUserComponent
        set(component) {
            mUserComponent = component
        }

    companion object {
        private const val TAG = "App"
        var app: App? = null
            private set
    }
}