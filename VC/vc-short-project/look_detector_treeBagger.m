%% Generate HOGs
hog_mat_width = 3;
hog_mat_height = 3;
hog_hist_number = 9;

hog_mat_size = hog_mat_width * hog_mat_height * hog_hist_number;

num_images_look = length(images_look);

look_hog = zeros(num_images_look, hog_mat_size);

for i = 1 : num_images_look
    I = squeeze(uint8(images_look(i,:,:)));
    look_hog(i,:) = HOG(I, hog_mat_width, hog_mat_height, hog_hist_number);
end

num_images_no_look = length(images_no_look);

no_look_hog = zeros(num_images_no_look, hog_mat_size);
for i = 1 : num_images_no_look
    I = squeeze(uint8(images_look(i,:,:)));
    no_look_hog(i,:) = HOG(I, hog_mat_width, hog_mat_height, hog_hist_number);
end
%% Train TreeBagger
num_treebagger_trees = 100;

num_images_look_to_test = 20;
no_look_to_look_ratio = 2;

num_images_no_look_to_test = num_images_look_to_test * no_look_to_look_ratio;

num_images_look_to_train = num_images_look - num_images_look_to_test;
num_images_no_look_to_train = ...
    num_images_no_look - num_images_no_look_to_test;


observations = vertcat(...
    look_hog(1:num_images_look_to_train,:,:), ...
    no_look_hog(1:num_images_no_look_to_train,:,:));

classifications = horzcat(...
    ones(1,num_images_look_to_train), ...
    zeros(1,num_images_no_look_to_train));

look_predictor = TreeBagger(num_treebagger_trees, observations, classifications');
%% Test TreeBagger
test_observations = vertcat(...
    look_hog(num_images_look_to_train + 1 : num_images_look,:,:), ...
    no_look_hog(num_images_no_look_to_train + 1 : num_images_no_look,:,:));

[predicted_classes, predicted_scores] = predict(look_predictor, test_observations);

results = horzcat(str2num(cell2mat(predicted_classes)),predicted_scores);

look_detection_accuracy = ...
    sum(results(1:num_images_look_to_test,:,:)) / num_images_look_to_test

no_look_detection_accuracy = ...
    1 - ...
    (sum(results(num_images_look_to_test + 1 : length(results),:,:)) / ...
    num_images_no_look_to_test)
