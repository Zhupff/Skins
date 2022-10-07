package zhupff.skins

import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.tasks.factory.dependsOn
import com.android.builder.model.Version.ANDROID_GRADLE_PLUGIN_VERSION
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.*
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.concurrent.atomic.AtomicBoolean

class SkinPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        if (!project.plugins.hasPlugin("com.android.application")) {
            throw IllegalStateException("${javaClass.simpleName} can only apply on application module.")
        }
        println("${javaClass.simpleName} apply on ${project.name}.")
        project.extensions.getByType(AppExtension::class.java).let { extensions ->
            extensions.sourceSets.getByName("main").assets.srcDirs(project.buildDir.resolve("skin-assets"))
            extensions.applicationVariants.all { variant ->
                val once = AtomicBoolean(false)
                variant.outputs.all { _ ->
                    if (once.compareAndSet(false, true)) {
                        project.tasks.create("${SkinPackageTask.NAME}Anchor_${variant.name.uppercase()}")
                            .also { variant.preBuildProvider.get().dependsOn(it) }
                    }
                }
            }
        }
    }
}


class SkinPackagePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        if (!project.plugins.hasPlugin("com.android.application")) {
            throw IllegalStateException("${javaClass.simpleName} can only apply on application module.")
        }
        println("${javaClass.simpleName} apply on ${project.name}.")
        project.extensions.getByType(AppExtension::class.java).applicationVariants.all { variant ->
            project.rootProject.allprojects.find {
                it.plugins.hasPlugin(SkinPlugin::class.java) || it.plugins.hasPlugin(SkinPlugin::class.java.name)
            }?.let { app ->
                val task = project.tasks.register("${SkinPackageTask.NAME}_${variant.name.uppercase()}", SkinPackageTask::class.java) {
                    it.outputFile = app.buildDir.resolve("skin-assets/${project.name}.skin")
                }
                app.tasks.named("${SkinPackageTask.NAME}Anchor_${variant.name.uppercase()}") {
                    task.dependsOn("packageRelease")
                    it.dependsOn(task)
                }
            }
        }
    }
}


@CacheableTask
open class SkinPackageTask : DefaultTask() {
    companion object {
        internal const val NAME: String = "SkinPackageTask"
    }

    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.NONE)
    val srcDir = project.projectDir.resolve("src")

    @OutputFile
    lateinit var outputFile: File

    @TaskAction
    fun action() {
        val agpFirstVersion = ANDROID_GRADLE_PLUGIN_VERSION.split('.').first().toInt()
        val apk = if (agpFirstVersion <= 4) {
            project.buildDir.resolve("outputs/apk/release")
                .listFiles()
                ?.find { it.name.endsWith(".apk") }
        } else {
            project.buildDir.resolve("intermediates/apk/release")
                .listFiles()
                ?.find { it.name.endsWith(".apk") }
        }
        apk?.let {
            Files.copy(
                it.toPath(),
                outputFile.toPath(),
                StandardCopyOption.COPY_ATTRIBUTES,
                StandardCopyOption.REPLACE_EXISTING)
        }
    }
}