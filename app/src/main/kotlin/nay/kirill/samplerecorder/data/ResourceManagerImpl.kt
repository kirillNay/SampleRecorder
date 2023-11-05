package nay.kirill.samplerecorder.data

import android.content.Context
import nay.kirill.samplerecorder.domain.ResourceManager

internal class ResourceManagerImpl(
    private val context: Context
) : ResourceManager {

    override fun getString(id: Int, vararg formatArgs: Any) =
        context.resources.getString(id, *formatArgs)

}