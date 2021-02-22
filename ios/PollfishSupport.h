#if __has_include("RCTEventEmitter.h")
#import "RCTEventEmitter.h"
#else
#import <React/RCTEventEmitter.h>
#endif

#if __has_include("Pollfish.h")
#import "Pollfish.h"
#else
#import <pollfish/Pollfish.h>
#endif

@interface PollfishSupport : RCTEventEmitter <RCTBridgeModule>

@end
