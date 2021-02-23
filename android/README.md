
# react-native-pollfish-support

## Getting started

`$ yarn add ngocthien0507anttech/react-native-pollfish-support


### CONFIG
#### iOS

- add the code below into Podfile
```
pod 'Pollfish'
pod 'react-native-pollfish-support', :path => '../node_modules/react-native-pollfish-support'
```

- if you are using flipper, add the code below
```
 use_flipper!({ 'Flipper-Folly' => '2.3.0' }) # update this part
```

## Usage
```javascript
import Pollfish from 'react-native-pollfish-full';

  const POLLFISH_EVENT = {
    SURVEY_RECEIVED: 'surveyReceived',
    SURVEY_COMPLETED: 'surveyCompleted',
    SURVEY_NOT_ELIGIBLE: 'userNotEligible',
    SURVEY_NOT_AVAILABLE: 'surveyNotAvailable',
    SURVEY_OPENED: 'surveyOpened',
    SURVEY_CLOSED: 'surveyClosed'
  }
  const pollfishConfig = {
    pollfishApiKey: AppConfig.POLLFISH_API_KEY,
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
      //do something
    }
    const handleSurveyClosed = () => {
      //do something
    }
    const handleSurveyCompleted = () => {
      //do something
    }
    const handleSurveyNotAvailable = () => {
      //do something
    }
    const handleSurveyNotEligible = () => {
      //do something
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
  // ...
     <PTextButton onPress={openSurvey} />
  )
```
    
