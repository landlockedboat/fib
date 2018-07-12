%% Load images
load images_eyes
load images_no_eyes_1
load images_no_eyes_2
images_no_eyes = vertcat(images_no_eyes_1, images_no_eyes_2);

%% process webcam images
factor = 0.4;
path = 'happy-man.jpg';

I = imread(path);
I = rgb2gray(I);
I = imresize(I, factor);

imshow(I);
%% find eyes in random image

I = imread(path);
I = rgb2gray(I);
I = imresize(I, factor);

[size_y, size_x] = size(I);

eye_size_factor = 0.07;
eye_size = uint32(size_x * eye_size_factor);

steps_x = uint32(size_x / eye_size);
steps_y = uint32(size_y / eye_size);

images_sections = zeros([steps_x * steps_y, 100, 100]);
index_images_sections = 1;
Im = I;

eye_divisions = 3;
offset_step = uint32(eye_size / eye_divisions);

section_index_to_pos = [];
for offset_x = 0 : offset_step : (eye_divisions - 1) * offset_step
    for offset_y = 0 : offset_step : (eye_divisions - 1) * offset_step
        for y = 1 : steps_y - 1
            for x = 1 : steps_x - 1
                eye_x = ((x - 1) * eye_size) + offset_x;
                eye_y = ((y - 1) * eye_size) + offset_y;
                
                Im = insertMarker(Im, [eye_x, eye_y]);
                
                rect = [eye_x eye_y eye_size eye_size];
                
                image_section = imcrop(I, rect);
                image_section = imresize(image_section,[100 100]);
                images_sections(index_images_sections,:,:) = image_section;
                
                section_index_to_pos = vertcat(section_index_to_pos, [eye_x, eye_y]);
                
                index_images_sections = index_images_sections + 1;
            end
        end
    end
end


imshow(Im);

%% Show images
n = length(images_sections);
for index = 1 : n
    I =  uint32(squeeze(images_sections(index,:,:)));
    imshow(I,[]);
end
%% Get sections HOG
num_images_sections = length(images_sections);
sections_hog = zeros(num_images_sections, hog_mat_size);

for i = 1 : num_images_sections
    I = squeeze(uint32(images_sections(i,:,:)));
    sections_hog(i,:) = HOG(I, hog_mat_width, hog_mat_height, hog_hist_number);
end

[predicted_classes, predicted_scores] = predict(eye_predictor, sections_hog);

results = horzcat(str2num(cell2mat(predicted_classes)),predicted_scores);

%% Show result
I = imread(path);
I = rgb2gray(I);
I = imresize(I, factor);

for i = 1 : length(results)
    res = results(i);
    if(res == 1)
        pos = section_index_to_pos(i, :);
        pos_x = pos(1);
        pos_y = pos(2);
        
        fprintf("Found eye on index %d; [%d, %d]\n", i, pos_x, pos_y);
        
        conf = results(i, 3);
        
        if(conf > 0.65)
            text = num2str(results(i, 3));
            
            color = 'green';
            
            if(conf < 0.75)
                color = 'red';
            else
                if(conf < 0.85)
                    color = 'yellow';
                end
            end
            
            I = insertShape(I, 'Rectangle', [pos_x, pos_y, eye_size, eye_size], ...
                'Color', color);
            I = insertText(I, [pos_x, pos_y + eye_size], text, 'FontSize',10, ...
                'BoxColor', color);
            end
        

    end
end

imshow(I);
%% Generate HOGs
hog_mat_width = 5;
hog_mat_height = 5;
hog_hist_number = 5;
num_treebagger_trees = 100;

hog_mat_size = hog_mat_width * hog_mat_height * hog_hist_number;

% eyes
num_images_eyes = length(images_eyes);
eyes_hog = zeros(num_images_eyes, hog_mat_size);

for i = 1 : num_images_eyes
    I = squeeze(uint32(images_eyes(i,:,:)));
    eyes_hog(i,:) = HOG(I, hog_mat_width, hog_mat_height, hog_hist_number);
end

% no eyes
num_images_no_eyes = length(images_no_eyes);
no_eyes_hog = zeros(num_images_no_eyes, hog_mat_size);

for i = 1 : num_images_no_eyes
    I = squeeze(uint32(images_no_eyes(i,:,:)));
    no_eyes_hog(i,:) = HOG(I, hog_mat_width, hog_mat_height, hog_hist_number);
end

%% Train TreeBagger
observations = vertcat(...
    eyes_hog, no_eyes_hog);

classifications = horzcat(...
    ones(1, num_images_eyes), ...
    zeros(1, num_images_no_eyes));

eye_predictor = TreeBagger(num_treebagger_trees, observations, classifications');

save eye_predictor