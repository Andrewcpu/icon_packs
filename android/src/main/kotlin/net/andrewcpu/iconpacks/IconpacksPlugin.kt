package net.andrewcpu.iconpacks

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import java.io.*

/** IconpacksPlugin */
public class IconpacksPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var mContext: Context
  private lateinit var packManager: IconPackManager

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "iconpacks")
    channel.setMethodCallHandler(this);
    mContext = flutterPluginBinding.applicationContext
    packManager = IconPackManager(mContext)
  }

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), "iconpacks")
      channel.setMethodCallHandler(IconpacksPlugin())
    }
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    }
    else if (call.method == "getIconPacks") {
      var packs: Map<String, IconPackManager.IconPack> = packManager.getAvailableIconPacks(true)
      result.success(listOf(packs.keys).toString())
    }

    else if(call.method.substring(0, 7) == "getIcon"){
      println(call.arguments)
      var iconPack: String = call.argument<String>("icon_pack")!!
      if(iconPack == "default" || iconPack == ""){
        result.success("default")
      }
      else{
        var pckg: String = call.argument<String>("application")!!
        var def= Bitmap.createBitmap(100, 100, Bitmap.Config.ALPHA_8)
        var output = (packManager.getAvailableIconPacks(false)[iconPack] as IconPackManager.IconPack).getIconForPackage(pckg, def)
        if (output == def){
          result.success("null");
        }
        else{
          var uri = bitmapToFile(output as Bitmap)
          result.success(uri)
        }
      }
    }else {
      result.notImplemented()
    }
  }
  private fun bitmapToFile(bitmap: Bitmap): ByteArray {
    // Get the context wrapper
    val wrapper = ContextWrapper(mContext)
    try{
      // Compress the bitmap and save in jpg format
      val stream: ByteArrayOutputStream = ByteArrayOutputStream()
      bitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
      stream.flush()
      stream.close()
      return stream.toByteArray()
    }catch (e: IOException){
      e.printStackTrace()
    }
    return byteArrayOf()

    // Return the saved bitmap uri
  }
  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
