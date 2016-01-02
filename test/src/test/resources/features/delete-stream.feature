Feature: Delete a stream

Scenario: Non existing
    Given the following streams don't exist
    | Stream Name       |
    | NameDoesNotMatter |
    When the following deletes are executed
    | Stream Name       | Hard Delete | Expected Version   | Expected Exception            | 
    | NameDoesNotMatter | true        | ANY                | -                             |
    | NameDoesNotMatter | true        | NO_OR_EMPTY_STREAM | -                             |
    | NameDoesNotMatter | false       | ANY                | -                             |
    | NameDoesNotMatter | false       | NO_OR_EMPTY_STREAM | -                             |
    | NameDoesNotMatter | true        | 1                  | WrongExpectedVersionException |
    | NameDoesNotMatter | false       | 1                  | WrongExpectedVersionException |
    Then this should give the expected results

Scenario: Already existing
    Given the following streams are created and a single event is appended to each
    | Stream Name     | 
    | DeleteExisting1 | 
    | DeleteExisting2 |
    | DeleteExisting3 |
    | DeleteExisting4 |
    | DeleteExisting5 |
    | DeleteExisting6 |
    | DeleteExisting7 |
    | DeleteExisting8 |
    When the following deletes are executed
    | Stream Name     | Hard Delete | Expected Version   | Expected Exception            | 
    | DeleteExisting1 | true        | ANY                | -                             |
    | DeleteExisting2 | true        | NO_OR_EMPTY_STREAM | WrongExpectedVersionException |
    | DeleteExisting2 | false       | ANY                | -                             |
    | DeleteExisting4 | false       | NO_OR_EMPTY_STREAM | WrongExpectedVersionException |
    | DeleteExisting5 | true        | 0                  | -                             |
    | DeleteExisting6 | false       | 0                  | -                             |
    | DeleteExisting7 | true        | 1                  | WrongExpectedVersionException |
    | DeleteExisting8 | false       | 1                  | WrongExpectedVersionException |
    Then this should give the expected results

Scenario: Read after delete
    Given the following streams are created and a single event is appended to each 
    | Stream Name         |
    | ReadAfterHardDelete |
    | ReadAfterSoftDelete |
    When the following deletes are executed
    | Stream Name         | Hard Delete | Expected Version | Expected Exception | 
    | ReadAfterHardDelete | true        | ANY              | -                  |
    | ReadAfterSoftDelete | false       | ANY              | -                  |
    Then this should give the expected results
    And following streams should not exist
    | Stream Name         |
    | ReadAfterHardDelete |
    | ReadAfterSoftDelete |
    And reading forward from the following streams should raise the given exceptions
    | Stream Name         | Start | Count | Expected Exception       | 
    | ReadAfterHardDelete | 1     | 1     | StreamDeletedException   |
    | ReadAfterSoftDelete | 1     | 1     | StreamNotFoundException  |

Scenario: Delete after delete
    Given the following streams are created and a single event is appended to each 
    | Stream Name            |
    | DeleteAfterHardDelete1 |
    | DeleteAfterHardDelete2 |
    | DeleteAfterHardDelete3 |
    | DeleteAfterHardDelete4 |
    | DeleteAfterSoftDelete1 |
    | DeleteAfterSoftDelete2 |
    | DeleteAfterSoftDelete3 |
    | DeleteAfterSoftDelete4 |
    When the following deletes are executed
    | Stream Name            | Hard Delete | Expected Version | Expected Exception | 
    | DeleteAfterHardDelete1 | true        | ANY              | -                  |
    | DeleteAfterHardDelete2 | true        | ANY              | -                  |
    | DeleteAfterHardDelete3 | true        | ANY              | -                  |
    | DeleteAfterHardDelete4 | true        | ANY              | -                  |
    | DeleteAfterSoftDelete1 | false       | ANY              | -                  |
    | DeleteAfterSoftDelete2 | false       | ANY              | -                  |
    | DeleteAfterSoftDelete3 | false       | ANY              | -                  |
    | DeleteAfterSoftDelete4 | false       | ANY              | -                  |
    Then this should give the expected results
    And executing the following deletes should have the given result
    | Stream Name            | Hard Delete | Expected Version | Expected Exception      | 
    | DeleteAfterHardDelete1 | true        | ANY              | StreamDeletedException  |
    | DeleteAfterHardDelete2 | false       | ANY              | StreamDeletedException  |
    | DeleteAfterHardDelete3 | true        | 0                | StreamDeletedException  |
    | DeleteAfterHardDelete4 | false       | 0                | StreamDeletedException  |
    | DeleteAfterSoftDelete1 | true        | ANY              | -                       |
    | DeleteAfterSoftDelete2 | false       | ANY              | -                       |
    | DeleteAfterSoftDelete3 | true        | 0                | -                       |
    | DeleteAfterSoftDelete4 | false       | 0                | -                       |
    