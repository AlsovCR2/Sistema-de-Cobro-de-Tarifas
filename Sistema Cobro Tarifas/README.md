# investigacion-06
# Ticket fare collection

## Architecture Characteristics to prioritize

- Maintainability
- Testability
- Scalability
- Reliability

## Context

Capsule Corporation is implementing a fare collection system using NFC cards. These cards allow for the wireless and secure storage of information, enabling transactions. The implementation involves NFC cards interacting with a Point of Service (POS) on each bus. This POS will be developed through an application in the chosen programming language or technology. Eventually, these POS systems must synchronize information with the company's central system to apply card charges. The encryption on the NFC card ensures the reliability and security of card data.

One challenge with this implementation is the lack of internet connection at some service points. Thus, it is necessary to ensure that NFC card information can be stored in the POS and synchronized with the central system when internet access is available. Additionally, it is crucial to avoid long waiting times when boarding a bus while synchronizing information.

## Problem to Solve

Given this scenario, a solution is sought for the asynchronous sending of card information. A mechanism or algorithm must be devised to record transactions and eventually synchronize them with the central system. Consideration must also be given to the possibility of communication errors when sending information, depending on the bus's location. Another factor to consider is the variable demand on the central system at specific times; therefore, asynchronous communication is recommended to prevent system centralization saturation.

## Preconditions

- Generate a client app to be used as a POS. It could be generated using any technology, for this case UI is not relevant.
- Backend not necessary have to persiste the data, it needs to process all the data in the right order. It's enough to simulate the data persistence.

## Scoring Parameters

| Characteristic  | Description                                                                                    | Scoring Criteria                                                                                                                                                                                                            |
|-----------------|------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Maintainability | Ease of maintaining and updating the fare collection system over time.                         | - Well-documented code: 3 points<br>- Modularity and clear structure: 2 points<br>- Lack of documentation or unclear structure: 1 point                                                                                     |
| Testability     | Ease of testing the fare collection system, including various scenarios and error handling.    | - Comprehensive test coverage for different scenarios: 3 points<br>- Easy to add new test cases: 2 points<br>- Limited test coverage or difficult to extend tests: 1 point                                                  |
| Scalability     | Ability of the system to handle a growing number of transactions and users.                    | - Easily scalable to accommodate increased transactions: 3 points<br>- Some scalability features, but with limitations: 2 points<br>- Limited scalability, potential issues with increased load: 1 point                    |
| Reliability     | Dependability of the fare collection system in handling transactions and data synchronization. | - High reliability with minimal errors and consistent data synchronization: 3 points<br>- Occasional errors with data synchronization or transaction handling: 2 points<br>- Frequent errors impacting reliability: 1 point |
