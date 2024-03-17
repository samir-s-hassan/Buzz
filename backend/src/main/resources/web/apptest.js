"use strict";
describe('NewEntryForm', function () {
    var newEntryForm;
    beforeEach(function () {
        // Initialize a new NewEntryForm instance before each test
        newEntryForm = new NewEntryForm();
    });
    it('should show an error if idea is empty', function () {
        // Mock the alert function
        spyOn(window, 'alert');
        // Simulate a form submission with an empty idea
        var formMock = {
            value: '',
        };
        var inputMock = {
            value: formMock,
        };
        spyOn(document, 'getElementById').and.returnValue(inputMock);
        // Call the submitForm method
        newEntryForm.submitForm();
        // Verify that the alert function was called with the expected message
        expect(window.alert).toHaveBeenCalledWith('Error: Idea or message is not valid');
    });
    it('should show an error if idea exceeds 2048 characters', function () {
        // Mock the alert function
        spyOn(window, 'alert');
        // Simulate a form submission with an idea exceeding 2048 characters
        var longIdea = 'A'.repeat(2049); // Create an idea with more than 2048 characters
        var formMock = {
            value: longIdea,
        };
        var inputMock = {
            value: formMock,
        };
        spyOn(document, 'getElementById').and.returnValue(inputMock);
        // Call the submitForm method
        newEntryForm.submitForm();
        // Verify that the alert function was called with the expected message
        expect(window.alert).toHaveBeenCalledWith('Error: Idea or message is not valid');
    });
    afterEach(function () {
        // Clean up any spies or resources after each test
    });
});
// // //   In this test, we check whether the buttons method in the ElementList class correctly creates a button fragment with the expected attributes. Additionally, we test the clickDelete method by simulating a click on a delete button and ensuring that it issues the correct AJAX DELETE request. Currently the clickDelete button is not visisble, but it is easily implementable.
// //   describe('ElementList', function () {
// //     let mainList;
// //     beforeEach(function () {
// //       // Initialize a new ElementList instance before each test
// //       mainList = new ElementList();
// //     });
// //     it('should create and return a button fragment', function () {
// //       // Create a sample data object with an ID and like count
// //       const sampleData = {
// //         mId: '123',
// //         mLikes: 42,
// //       };
// //       // Call the buttons method to create a button fragment
// //       const buttonFragment = mainList.buttons(sampleData.mId, sampleData.mLikes);
// //       // Verify that the fragment contains a button with the correct like count
// //       const buttonElement = buttonFragment.querySelector('button.likeBtn');
// //       expect(buttonElement).not.toBeNull();
// //       expect(buttonElement.getAttribute('data-value')).toBe(sampleData.mId);
// //       expect(buttonElement.innerHTML).toBe(sampleData.mLikes.toString());
// //     });
// //     it('should handle clickDelete method and issue an AJAX DELETE request', function () {
// //       // Mock the fetch function
// //       spyOn(window, 'fetch').and.returnValue(Promise.resolve({ ok: true, json: () => Promise.resolve({}) }));
// //       // Simulate a click on a delete button
// //       const deleteButton = document.createElement('button');
// //       deleteButton.classList.add('delbtn');
// //       deleteButton.setAttribute('data-value', '456');
// //       // Call the clickDelete method
// //       mainList.clickDelete({ target: deleteButton });
// //       // Verify that the fetch function was called with the expected URL and method
// //       expect(window.fetch).toHaveBeenCalledWith('https://team-pioneers.dokku.cse.lehigh.edu/ideas/456', {
// //         method: 'DELETE',
// //         headers: {
// //           'Content-type': 'application/json; charset=UTF-8',
// //         },
// //       });
// //     });
// //     afterEach(function () {
// //       // Clean up any spies or resources after each test
// //     });
// //   });
