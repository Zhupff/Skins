package zhupff.skins

import android.content.ContextWrapper
import android.view.View
import androidx.annotation.MainThread
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean
import zhupff.skins.core.R

@MainThread
class SkinWrapper(val view: View) : View.OnAttachStateChangeListener, Observer<Skin> {
    companion object {

        @JvmStatic
        @MainThread
        fun get(view: View): SkinWrapper? = view.getTag(R.id.skin_wrapper_tag) as? SkinWrapper

        @JvmStatic
        @MainThread
        fun bind(view: View): SkinWrapper {
            val tag = view.getTag(R.id.skin_wrapper_tag)
            if (tag != null) {
                if (tag is SkinWrapper)
                    return tag
                else
                    throw IllegalStateException("View($view) already bind something(${tag}).")
            }
            return SkinWrapper(view)
        }

        @JvmStatic
        @MainThread
        fun unbind(view: View) {
            (view.getTag(R.id.skin_wrapper_tag) as? SkinWrapper)?.release()
        }
    }

    val dispatcher: SkinDispatcher<*> = this.run {
        var parent = view.parent
        while (parent != null) {
            if (parent is View) {
                get(parent)?.let { wrapper ->
                    return@run wrapper.dispatcher
                }
            }
            parent = parent.parent
        }

        var context = view.context
        while (context is ContextWrapper) {
            if (context is SkinDispatcher<*>)
                return@run context
            context = context.baseContext
        }

        throw IllegalStateException("Can't find SkinDispatcher for ${view}.")
    }

    private val isAttached: AtomicBoolean = AtomicBoolean(false)

    private val tasks: HashMap<String, Runnable> = HashMap(4)

    private var skinTimestamp: Long = -1L

    init {
        view.getTag(R.id.skin_wrapper_tag)?.let {
            throw IllegalStateException("View($view) already bind something(${it}).")
        }
        view.setTag(R.id.skin_wrapper_tag, this)
        view.addOnAttachStateChangeListener(this)
        onViewAttachedToWindow(view)
    }

    @MainThread
    fun addTask(name: String, task: Runnable) = apply {
        tasks[name] = task.also { it.run() }
    }

    @MainThread
    fun removeTask(name: String) = apply {
        tasks.remove(name)
    }

    @MainThread
    fun release() {
        view.setTag(R.id.skin_wrapper_tag, null)
        view.removeOnAttachStateChangeListener(this)
        onViewDetachedFromWindow(view)
    }

    override fun onViewAttachedToWindow(v: View?) {
        if (isAttached.compareAndSet(false, true)) {
            dispatcher.getCurrentSkin().observeForever(this)
        }
    }

    override fun onViewDetachedFromWindow(v: View?) {
        if (isAttached.compareAndSet(true, false)) {
            dispatcher.getCurrentSkin().removeObserver(this)
        }
    }

    override fun onChanged(skin: Skin?) {
        val target = skin ?: dispatcher.getDefaultSkin()
        if (skinTimestamp != target.timestamp) {
            skinTimestamp = target.timestamp
            tasks.values.forEach { it.run() }
        }
    }
}