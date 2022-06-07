Pod::Spec.new do |spec|
    spec.name                     = 'core'
    spec.version                  = '1.0.1'
    spec.homepage                 = 'https://github.com/Darkos-den/MVU-Core'
    spec.source                   = { :git => 'https://github.com/Darkos-den/MVU-Core.git', :tag => 'v1.0.0' }
    spec.authors                  = { 'Darkos' => 'loboda.deni@gmail.com' }
    spec.license                  = { :type => 'BSD' }
    spec.summary                  = 'Some description for the Shared Module'

    spec.vendored_frameworks      = "framework/core.framework"
    spec.libraries                = "c++"
    spec.module_name              = "#{spec.name}_umbrella"

    spec.ios.deployment_target = '14.1'

                

    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':core',
        'PRODUCT_MODULE_NAME' => 'core',
    }

    spec.script_phases = [
        {
            :name => 'Build core',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                if [ "YES" = "$COCOAPODS_SKIP_KOTLIN_BUILD" ]; then
                  echo "Skipping Gradle build task invocation due to COCOAPODS_SKIP_KOTLIN_BUILD environment variable set to \"YES\""
                  exit 0
                fi
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:syncFramework \
                    -Pkotlin.native.cocoapods.platform=$PLATFORM_NAME \
                    -Pkotlin.native.cocoapods.archs="$ARCHS" \
                    -Pkotlin.native.cocoapods.configuration=$CONFIGURATION
            SCRIPT
        }
    ]
end