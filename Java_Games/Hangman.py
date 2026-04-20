from tkinter import messagebox
import random

class HangmanGame:
    def __init__(self, root):
        self.root = root
        self.root.title("NIIT Modern Hangman")
        self.root.geometry("600x500")
        
        # Game Variables
        self.words = ["DOCKER", "KUBERNETES", "PYTHON", "JAVA", "CYPRESS", "MICROSERVICES"]
        self.target_word = random.choice(self.words)
        self.guessed_letters = []
        self.attempts_left = 6

        # UI Elements
        self.canvas = tk.Canvas(root, width=200, height=250, bg="white")
        self.canvas.pack(pady=20)
        
        self.word_display = tk.Label(root, text=self.get_display_word(), font=("Helvetica", 30))
        self.word_display.pack(pady=20)
        
        self.entry = tk.Entry(root, font=("Helvetica", 18))
        self.entry.pack(pady=10)
        
        self.guess_btn = tk.Button(root, text="Guess Letter", command=self.make_guess, bg="#4CAF50", fg="white")
        self.guess_btn.pack()

    def get_display_word(self):
        return " ".join([char if char in self.guessed_letters else "_" for char in self.target_word])

    def draw_hangman(self):
        parts = [
            lambda: self.canvas.create_oval(80, 50, 120, 90, width=3), # Head
            lambda: self.canvas.create_line(100, 90, 100, 160, width=3), # Body
            lambda: self.canvas.create_line(100, 100, 70, 130, width=3), # Left Arm
            lambda: self.canvas.create_line(100, 100, 130, 130, width=3), # Right Arm
            lambda: self.canvas.create_line(100, 160, 70, 200, width=3), # Left Leg
            lambda: self.canvas.create_line(100, 160, 130, 200, width=3), # Right Leg
        ]
        index = 6 - self.attempts_left - 1
        if index >= 0: parts[index]()

    def make_guess(self):
        letter = self.entry.get().upper()
        self.entry.delete(0, tk.END)
        
        if letter and letter not in self.guessed_letters:
            self.guessed_letters.append(letter)
            if letter not in self.target_word:
                self.attempts_left -= 1
                self.draw_hangman()
            
            self.word_display.config(text=self.get_display_word())
            self.check_game_over()

    def check_game_over(self):
        if "_" not in self.get_display_word():
            messagebox.showinfo("Winner!", "You saved the man!")
            self.root.destroy()
        elif self.attempts_left <= 0:
            messagebox.showerror("Game Over", f"The word was: {self.target_word}")
            self.root.destroy()

if __name__ == "__main__":
    root = tk.Tk()
    game = HangmanGame(root)
    root.mainloop()
