module Main where

import Data.List(nub)

data Formula = Variable Char 
                | Formula :∧ Formula
                | Formula :∨ Formula
                | Formula :→ Formula
                | NOT Formula 

infixl 8 :∨
infixl 7 :∧
infixr 6 :→

data Sequent = [Formula] :⊢ [Formula] 

data ProofTree = Leaf Sequent
            | Node ProofTree Sequent ProofTree
            | Nil 

instance Show Formula where
    show (Variable x) = [x]
    show (a :→ b) = "(" ++ show a ++ " → " ++ show b ++ ")"
    show (a :∧ b) = show a ++ " ∧ " ++ show b
    show (a :∨ b) = show a ++ " ∨ " ++ show b
    show (NOT a) = "¬" ++ show a

instance Show Sequent where
    show (ants :⊢ sucs) = helper ants ++ " ⊢ " ++ helper sucs
      where 
        helper [] = "_"
        helper [x] = show x
        helper (x:xs) = show x ++ ", " ++ helper xs

instance Show ProofTree where
    show Nil = ""
    show (Leaf sequent) = show sequent
    show (Node son1 sequent son2) = show sequent ++ "\n" ++ show son1 ++ "  " ++ show son2

rule :: Sequent -> [Sequent]
rule (((alpha :→ beta):gamma) :⊢ delta) = [ gamma :⊢ (alpha:delta),(beta:gamma) :⊢ delta]
rule (gamma :⊢ ((alpha :→ beta):delta)) = [(alpha:gamma) :⊢ (beta:delta)]
rule (((alpha :∧ beta):gamma) :⊢ delta) = [(alpha:beta:gamma) :⊢ delta]
rule (gamma :⊢ ((alpha :∧ beta):delta)) = [gamma :⊢ (alpha:delta), gamma :⊢ (beta:delta)]
rule (((alpha :∨ beta):gamma) :⊢ delta) = [(alpha:gamma) :⊢ delta, (beta:gamma) :⊢ delta]
rule (gamma :⊢ ((alpha :∨ beta):delta)) = [gamma :⊢ (alpha:beta:delta)]
rule ((NOT alpha:gamma) :⊢ delta) = [gamma :⊢ (alpha:delta)]
rule (gamma :⊢ (NOT alpha:delta)) = [(alpha:gamma) :⊢ delta]
rule ((arg@(Variable _):gamma) :⊢ delta) = (\(ants :⊢ sucs) -> (arg:ants) :⊢ sucs) <$> rule (gamma :⊢ delta)
rule (gamma :⊢ (arg@(Variable _):delta)) = (\(ants :⊢ sucs) -> ants :⊢ (arg:sucs)) <$> rule (gamma :⊢ delta)
rule ([] :⊢ []) = []

solve :: Sequent -> ProofTree 
solve sequent = helper $ rule sequent
    where 
        helper [] = Leaf sequent
        helper [arg1] = Node (solve arg1) sequent Nil
        helper [arg1,arg2] = Node (solve arg1) sequent (solve arg2)

isTautology :: ProofTree -> Bool
isTautology Nil = True
isTautology (Node son1 _ son2) = isTautology son1 && isTautology son2
isTautology (Leaf sequent) = isAxiom sequent

isAxiom ([] :⊢ _) = False
isAxiom (ants :⊢ sucs) = or (fmap (`helper` sucs) ants)
    where 
    helper _ [] = False
    helper arg@(Variable x) (Variable y:xs)
                              | x /= y    = helper arg xs
                              | otherwise = True
    helper _ _ = False

findCounterExample :: Formula -> ProofTree -> [(Char, Bool)]
findCounterExample formula tree = 
    getInterpretation (findAllVariables formula) (getValues $ head $ findAllLeaves tree) 
    where 
      findAllLeaves Nil = []
      findAllLeaves (Leaf sequent) = if isAxiom sequent then [] else [sequent]
      findAllLeaves (Node son1 _ son2) = findAllLeaves son1 ++ findAllLeaves son2
      
      findAllVariables (Variable x) = [x]
      findAllVariables (a :→ b) = findAllVariables a ++ findAllVariables b
      findAllVariables (a :∨ b) = findAllVariables a ++ findAllVariables b
      findAllVariables (a :∧ b) = findAllVariables a ++ findAllVariables b
      findAllVariables (NOT a) = findAllVariables a

      getValues (ants :⊢ sucs) = ((\(Variable x) -> (x, True)) <$> ants) ++ ((\(Variable x) -> (x, False)) <$> sucs)
      getInterpretation vars values = nub $ fmap f vars
        where 
          f var = case lookup var values of 
                    Nothing -> (var,False)
                    Just value -> (var,value) 

createProofTree :: Formula -> ProofTree
createProofTree formula = solve $ [] :⊢ [formula]

printProofTree :: ProofTree -> IO ()
printProofTree tree = do
    putStrLn "ProofTree:"
    putStrLn $ helper tree 1
    where
      helper Nil _ = ""
      helper (Leaf sequent) k = indent k ++ show sequent ++ "\n"
      helper (Node son1 sequent son2) k = indent k ++ show sequent ++ "\n" ++ helper son1 (k+1) ++ helper son2 (k+1)
      indent k = replicate (4*k) ' '

validateProofTree :: Formula -> ProofTree -> IO ()
validateProofTree formula tree = 
    if isTautology tree
    then do
        putStrLn "Answer: is tautology"
        printProofTree tree
    else do 
        putStrLn "Answer: is not tautology"
        printProofTree tree
        putStrLn $ "Counterexample: " ++ show (findCounterExample formula tree)

test :: Formula -> IO ()
test formula = do
    putStrLn $ "Input: " ++ show formula
    validateProofTree formula (createProofTree formula)
    putStrLn ""
    
example1 :: Formula 
example1 = NOT (Variable 'p' :∧ Variable 'q') :→ Variable 'p'

example2 :: Formula 
example2 = NOT (Variable 'p' :∧ Variable 'q') :→ NOT (Variable 'p') :∨ NOT (Variable 'q')

example3 :: Formula
example3 = Variable 'p' :→ Variable 'p'

example4 :: Formula
example4 = Variable 'p' :∨ NOT (Variable 'p')

example5 :: Formula
example5 = Variable 'p' :→ Variable 'q' :→ Variable 'p' :∧ Variable 'q'

example6 :: Formula
example6 = Variable 'p' :→ Variable 'q' :→  Variable 'p'

example7 :: Formula
example7 = Variable 'p' :→ Variable 'p' :∨ Variable 'q'

example8 :: Formula 
example8 = (Variable 'p' :→ Variable 'q') :→ (Variable 'p' :→ NOT (Variable 'q')) :→ NOT (Variable 'p')

example9 :: Formula 
example9 = (Variable 'a' :→ (Variable 'b' :→ Variable 'c')) :→ (Variable 'a' :→ Variable 'b') :→ (Variable 'a' :→ Variable 'c')

example10 :: Formula 
example10 = NOT $ (Variable 'p' :→ Variable 'p') :→ Variable 'q'

main :: IO ()
main = do
    test example1
    test example2
    test example3
    test example4
    test example5
    test example6
    test example7
    test example8
    test example9
    test example10