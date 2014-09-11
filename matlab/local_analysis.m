% For each point, look for a dataset surrounding it

dist = 2;
threshold = 20;

median_error = [];

for index = 1:1547
    
    center = target(:,index);
    
    % Collect data around the center point
    round_input = [];
    round_target = [];
    
    for sindex = 1:1547
        if sindex == index
            continue;
        end
        
        current = target(:,sindex);
        
        if norm(current - center) <= dist
            cat(2, round_input, input(:,sindex));
            cat(2, round_target, target(:,sindex));
        end
    end
    
    % Ignore input with to less data
    [m,n] = size(round_input);
    if n < threshold 
        continue;
    end
    
    % Train neural network around the center point
    % Create a Fitting Network
    hiddenLayerSize = 30;
    net = fitnet(hiddenLayerSize);
    
    % Choose Input and Output Pre/Post-Processing Functions
    % For a list of all processing functions type: help nnprocess
    net.inputs{1}.processFcns = {'removeconstantrows','mapminmax'};
    net.outputs{2}.processFcns = {'removeconstantrows','mapminmax'};
    
    
    % Setup Division of Data for Training, Validation, Testing
    % For a list of all data division functions type: help nndivide
    net.divideFcn = 'dividerand';  % Divide data randomly
    net.divideMode = 'sample';  % Divide up every sample
    net.divideParam.trainRatio = 80/100;
    net.divideParam.valRatio = 10/100;
    net.divideParam.testRatio = 10/100;
    
    % For help on training function 'trainlm' type: help trainlm
    % For a list of all training functions type: help nntrain
    net.trainFcn = 'trainlm';  % Levenberg-Marquardt
    
    % Choose a Performance Function
    % For a list of all performance functions type: help nnperformance
    net.performFcn = 'mse';  % Mean squared error
    
    % Choose Plot Functions
    % For a list of all plot functions type: help nnplot
    % net.plotFcns = {'plotperform','plottrainstate','ploterrhist', ...
    %    'plotregression', 'plotfit'};
    
    
    % Train the Network
    [net,tr] = train(net,inputs,targets);
    
    % Test the Network
    outputs = net(inputs);
    errors = gsubtract(targets,outputs);
    performance = perform(net,targets,outputs);
    
    % Recalculate Training, Validation and Test Performance
    trainTargets = targets .* tr.trainMask{1};
    valTargets = targets  .* tr.valMask{1};
    testTargets = targets  .* tr.testMask{1};
    trainPerformance = perform(net,trainTargets,outputs)
    valPerformance = perform(net,valTargets,outputs)
    testPerformance = perform(net,testTargets,outputs)
    
    % View the Network
    
    % Plots
    % Uncomment these lines to enable various plots.
    %figure, plotperform(tr)
    %figure, plottrainstate(tr)
    %figure, plotfit(net,inputs,targets)
    %figure, plotregression(targets,outputs)
    %figure, ploterrhist(errors)
    
    % Record the median error of each network
    

end