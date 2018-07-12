function [eye_detection_accuracy, no_eye_detection_accuracy] = ...
    eyeDetector(images_eyes, images_no_eyes, ...
    hog_mat_width, hog_mat_height, hog_hist_number, num_treebagger_trees)
% Generate HOGs
hog_mat_size = hog_mat_width * hog_mat_height * hog_hist_number;

num_images_eyes = length(images_eyes);

eyes_hog = zeros(num_images_eyes, hog_mat_size);

for i = 1 : num_images_eyes
    I = squeeze(uint8(images_eyes(i,:,:)));
    eyes_hog(i,:) = HOG(I, hog_mat_width, hog_mat_height, hog_hist_number);
end

num_images_no_eyes = length(images_no_eyes);
no_eyes_hog = zeros(num_images_no_eyes, hog_mat_size);

for i = 1 : num_images_no_eyes
    I = squeeze(uint8(images_no_eyes(i,:,:)));
    no_eyes_hog(i,:) = HOG(I, hog_mat_width, hog_mat_height, hog_hist_number);
end

% Train TreeBagger
num_images_eyes_to_test = 20;
no_eyes_to_eyes_ratio = 18;

num_images_no_eyes_to_test = num_images_eyes_to_test * no_eyes_to_eyes_ratio;

num_images_eyes_to_train = num_images_eyes - num_images_eyes_to_test;
num_images_no_eyes_to_train = ...
    num_images_no_eyes - num_images_no_eyes_to_test;


observations = vertcat(...
    eyes_hog(1:num_images_eyes_to_train,:,:), ...
    no_eyes_hog(1:num_images_no_eyes_to_train,:,:));

classifications = horzcat(...
    ones(1,num_images_eyes_to_train), ...
    zeros(1,num_images_no_eyes_to_train));

eye_predictor = TreeBagger(num_treebagger_trees, observations, classifications');

% Test TreeBagger
test_observations = vertcat(...
    eyes_hog(num_images_eyes_to_train + 1 : num_images_eyes,:,:), ...
    no_eyes_hog(num_images_no_eyes_to_train + 1 : num_images_no_eyes,:,:));

[predicted_classes, predicted_scores] = predict(eye_predictor, test_observations);

results = horzcat(str2num(cell2mat(predicted_classes)),predicted_scores);

eye_detection_accuracy = ...
    sum(results(1:num_images_eyes_to_test,:,:)) / num_images_eyes_to_test;

no_eye_detection_accuracy = ...
    1 - ...
    (sum(results(num_images_eyes_to_test + 1 : length(results),:,:)) / ...
    num_images_no_eyes_to_test);
end

