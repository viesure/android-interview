# Mobile Interview Assignment
## Overview
Use an API to fetch list of Articles, parse, persist to disk and present in a List. Users should be able to see the detail of articles by tapping on each list item.

## Mocked Articles API:
```curl
curl --request GET 'https://run.mocky.io/v3/5de1afae-d287-4210-a07e-eb73d55550bb'
```
This endpoint returns a dummy list of articles with the follosing structure:
```json
[{
  "id": 1,
  "title": "Realigned multimedia framework",
  "description": "nisl aenean lectus pellentesque eget nunc donec quis orci eget orci vitae mattis nibh ligula",
  "author": "sfolley0@nhs.uk",
  "release_date": "6/25/2018",
  "image": "http://dummyimage.com/366x582.png/5fa2dd/ffffff"
}]
```

## Acceptance criteria:
* User must be able to see the latest available articles whenever he/she launches the app.
* In case of data synchroniastion failure due to a **500 internal server error or network coverage issue** we need to retry **3 times with 2 seconds backoff delay** before showing an error message to user.
* User must be able to see the latest -locally- stored articles (if any) in case of data  synchroniastion failure.
* All locally stored data should be **encrypted**.
* The article list must be sorted by the given date.
* Article list contains the following items:
  * Article title: `title`
  * First two lines of `description`
  * Article header image : `image`
* User must be able to see the full detail of articles by tapping each list item.
* The following items must be shown in the article's detail page:
  * Article header image : `image`
  * Article title: `title`
  * Article content : `description`
  * Author e-mail address: `author`
  * Article's release date : `date` (this date must be shown by `Wed, Jul 8, '20` format)

## Design mockup:
![design](https://i.ibb.co/5WzcrWR/Screenshot-2020-03-28-at-15-25-52.png")

## Minimum architecture requirement(s)
* This app should follow the multi-module architecture.
* The app must at least have two modules (articles and details)

## Minimum technical Expectation
* Minimum **80%** test coverage. (You need to attach the coverage report after finishing the task)
* Clean architecture and coding style.
* Use Hilt/Dagger for dependency injection.
* App should support `API 23` and above.
* As a mobile engineer you should be able to detect/prevent error and possible edge cases.

## Next Step
Our mobile development team will review your task carefully and contact you as soon as possible.

