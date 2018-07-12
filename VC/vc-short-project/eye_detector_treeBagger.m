%% Load images
load images_eyes
load images_no_eyes_1
load images_no_eyes_2
images_no_eyes = vertcat(images_no_eyes_1, images_no_eyes_2);
%% Get results

num_treebagger_trees_step_size = 30;
hog_mat_size_step_size = 2;
hog_hist_number_step_size = 2;

init_num_treebagger_trees = 10;
init_hog_hist_number = 1;
init_hog_mat_size = 1;

num_steps = 3;
total_steps = num_steps ^ 3;
results = [];

curr_step = 1;

num_treebagger_trees = init_num_treebagger_trees;
hog_hist_number = init_hog_hist_number;
hog_mat_width = init_hog_mat_size;
hog_mat_height = init_hog_mat_size;

for i = 1 : num_steps
    hog_mat_width = init_hog_mat_size + hog_mat_size_step_size * (i - 1);
    hog_mat_height = init_hog_mat_size + hog_mat_size_step_size * (i - 1);
    for j = 1 : num_steps
        hog_hist_number = init_hog_hist_number + ...
            hog_hist_number_step_size * (j - 1);
        for k = 1 : num_steps
            num_treebagger_trees = ...
                init_num_treebagger_trees + num_treebagger_trees_step_size * (k - 1);
        
            tic;
            [eye_detection_accuracy, no_eye_detection_accuracy] = ...
                eyeDetector(images_eyes, images_no_eyes, ...
                hog_mat_width, hog_mat_height, hog_hist_number, num_treebagger_trees);
            
            res = [hog_mat_width, hog_hist_number, num_treebagger_trees, ...
                eye_detection_accuracy(3), no_eye_detection_accuracy(3), ...
                eye_detection_accuracy(1), no_eye_detection_accuracy(1)];
            
            results = vertcat(results, res)
            
            fprintf('Test %d of %d (%d%%)\n', curr_step, total_steps, ...
                uint8((curr_step  / total_steps) * 100));
            
            curr_step = curr_step + 1;
            toc
            
        end
    end
end