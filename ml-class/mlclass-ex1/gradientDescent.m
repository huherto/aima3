function [theta, J_history] = gradientDescent(X, y, theta, alpha, num_iters)
%GRADIENTDESCENT Performs gradient descent to learn theta
%   theta = GRADIENTDESENT(X, y, theta, alpha, num_iters) updates theta by 
%   taking num_iters gradient steps with learning rate alpha

% Initialize some useful values
m = length(y); % number of training examples
J_history = zeros(num_iters, 1);

for iter = 1:num_iters

    % ====================== YOUR CODE HERE ======================
    % Instructions: Perform a single gradient step on the parameter vector
    %               theta. 
    %
    % Hint: While debugging, it can be useful to print out the values
    %       of the cost function (computeCost) and gradient here.
    %

    sum1 = 0;
    for i = 1:m
      x = X(i, :);
      h = theta' * x';  
      sum1 = sum1 + (h - y(i))*x(1);
    endfor    
    
    sum2 = 0;
    for i = 1:m
      x = X(i, :);
      h = theta' * x';  
      sum2 = sum2 + (h - y(i))*x(2);
    endfor
    
    theta(1) = theta(1) - ((alpha/m) * sum1);
    theta(2) = theta(2) - ((alpha/m) * sum2);

%    computeCost(X, y, theta)

    % ============================================================

    % Save the cost J in every iteration    
    J_history(iter) = computeCost(X, y, theta);

end

end
