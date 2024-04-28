import pandas as pd
import typing
from scipy.stats import norm
import numpy as np
from matplotlib import pyplot as plt


def create_itemlist_beta(no_items: int, alpha: float, beta: float, name: str) -> pd.DataFrame:
    df = pd.DataFrame({"name": ["BETA"+str(i) for i in range(no_items)]})
    # df["name"] = ["BETA"+str(i) for i in range(no_items)]
    df["wert"] = np.random.beta(alpha, beta, no_items)
    # plot(df, "Beta Distribution", name, no_items)
    return df


def create_itemlist_lognormal(no_items: int, name: str) -> pd.DataFrame:
    df = pd.DataFrame({"name": ["LOGNORM"+str(i) for i in range(no_items)]})
    df["wert"] = np.random.lognormal(3, 1, no_items)
    # plot(df, "LogNormal Distribution", name, no_items)
    return df


def create_itemlist_normal(no_items: int, name: str) -> pd.DataFrame:
    df = pd.DataFrame({"name": ["NORMAL"+str(i) for i in range(no_items)]})
    df["wert"] = np.random.random(size=no_items)

    # plot(df, "Normal Distribution", name, no_items)
    return df


def plot(df: pd.DataFrame, title: str, name: str, no_items: int) -> None:
    plt.clf()
    plt.hist(df["wert"], bins=int(no_items/1000), density=True, alpha=0.7,
             color='skyblue', edgecolor='black')
    plt.xlabel('Value')
    plt.ylabel('Frequency')
    plt.title(f"{title} with {no_items} samples")
    plt.savefig(f"Figures/{name}.png")
    # plt.show()

    return df


def save_as_csv(df: pd.DataFrame, name: str) -> None:
    df.to_csv(f"Data/{name}", index=False)


def main():

    no_items = [8, 10, 12, 14, 16, 18, 20, 21, 22, 23, 24, 25, 2000]
    no_items = [8, 10]
    for i in range(len(no_items)):

        name = f"beta_dist_{no_items[i]}"
        df = create_itemlist_beta(no_items[i], 2.0, 4.0, name)
        name += ".csv"
        save_as_csv(df, name)

        name = f"lognormal_dist_{no_items[i]}"
        df = create_itemlist_lognormal(no_items[i], name)
        name += ".csv"
        save_as_csv(df, name)

        name = f"normal_dist_{no_items[i]}"
        df = create_itemlist_normal(no_items[i], name)
        name += ".csv"
        save_as_csv(df, name)


if __name__ == "__main__":
    main()
