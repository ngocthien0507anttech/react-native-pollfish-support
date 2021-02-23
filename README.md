
# react-native-pollfish-support

## Getting started

`$ yarn add ngocthien0507anttech/react-native-pollfish-support

### Mostly automatic installation

`$ react-native link react-native-pollfish-full`

### Manual installation
#### iOS

- add these code into Podfile:
```
pod 'react-native-pollfish-support', :path => '../node_modules/react-native-pollfish-support'
pod 'Pollfish'
```
- if you are using flipper
update this part: `use_flipper!({ 'Flipper-Folly' => '2.3.0' })`


## Usage
```javascript
import Pollfish from 'react-native-pollfish-support'

  const POLLFISH_EVENT = {
    SURVEY_RECEIVED: 'surveyReceived',
    SURVEY_COMPLETED: 'surveyCompleted',
    SURVEY_NOT_ELIGIBLE: 'userNotEligible',
    SURVEY_NOT_AVAILABLE: 'surveyNotAvailable',
    SURVEY_OPENED: 'surveyOpened',
    SURVEY_CLOSED: 'surveyClosed'
  }

  const pollfishConfig = {
    pollfishApiKey: 'POLLFISH_API_KEY',
    releaseMode: true,
    customMode: true,
    offerWallMode: false
  }

  const initPollfishSurvey = () =>
    Pollfish.initialize(
      pollfishConfig.pollfishApiKey,
      pollfishConfig.releaseMode,
      pollfishConfig.customMode,
      pollfishConfig.offerWallMode
    )

    const handleSurveyReceived = () => {
      // Do something
    }
    const handleSurveyClosed = () => {
      // Do something
    }
    const handleSurveyCompleted = () => {
      // Do something
    }
    const handleSurveyNotAvailable = () => {
      // Do something
    }
    const handleSurveyNotEligible = () => {
      // Do something
    }
    const openSurvey = () => {
      Pollfish.show()
    }

    const addPollfishListener = () => {
      Pollfish.addEventListener(
        POLLFISH_EVENT.SURVEY_RECEIVED,
        handleSurveyReceived
      )
      Pollfish.addEventListener(POLLFISH_EVENT.SURVEY_CLOSED, handleSurveyClosed)
      Pollfish.addEventListener(
        POLLFISH_EVENT.SURVEY_COMPLETED,
        handleSurveyCompleted
      )
      Pollfish.addEventListener(
        POLLFISH_EVENT.SURVEY_NOT_AVAILABLE,
        handleSurveyNotAvailable
      )
      Pollfish.addEventListener(
        POLLFISH_EVENT.SURVEY_NOT_ELIGIBLE,
        handleSurveyNotEligible
      )
    }

    useEffect(() => {
      initPollfishSurvey()
      addPollfishListener()
      return () => {
        Pollfish.removeAllListeners()
      }
    }, [])
    
    return (
      <PTextButton onPress={openSurvey} />
    )

```
  
