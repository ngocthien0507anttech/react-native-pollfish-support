require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-pollfish-support"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  react-native-pollfish-support
                   DESC
  s.homepage     = "https://github.com/ngocthien0507anttech/react-native-pollfish-support"
  # brief license entry:
  s.license      = "MIT"
  # optional - use expanded license entry instead:
  # s.license    = { :type => "MIT", :file => "LICENSE" }
  s.authors      = { "Ant Tech Ngoc Thien" => "ngoc-thien.nguyen@ant-tech.eu" }
  s.platforms    = { :ios => "9.0" }
  s.source       = { :git => "https://github.com/ngocthien0507anttech/react-native-pollfish-support", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,c,m,swift}"
  s.requires_arc = true

  s.dependency "React"
  # ...
  # s.dependency "..."
end

