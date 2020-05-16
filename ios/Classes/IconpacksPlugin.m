#import "IconpacksPlugin.h"
#if __has_include(<iconpacks/iconpacks-Swift.h>)
#import <iconpacks/iconpacks-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "iconpacks-Swift.h"
#endif

@implementation IconpacksPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftIconpacksPlugin registerWithRegistrar:registrar];
}
@end
